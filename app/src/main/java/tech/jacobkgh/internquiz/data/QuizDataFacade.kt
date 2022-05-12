package tech.jacobkgh.internquiz.data


import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import tech.jacobkgh.internquiz.data.jsondata.APITokenResponse
import tech.jacobkgh.internquiz.data.jsondata.CategoryQuestionSummary
import tech.jacobkgh.internquiz.data.jsondata.QuestionCollection
import tech.jacobkgh.internquiz.data.jsondata.QuizCategories
import tech.jacobkgh.internquiz.exceptions.NoMoreQuestionsException
import tech.jacobkgh.internquiz.model.QuizCategory
import tech.jacobkgh.internquiz.model.QuizQuestion
import tech.jacobkgh.internquiz.model.enumeration.QuizDifficulty

/**
 * "singleton" class to exposing functionality for retrieving data from https://opentdb.com
 */
class QuizDataFacade {


    companion object {

        //TODO: move api base URL to config resource?
        private inline fun <reified T> fetchDecodeJson(url: String): T {
            Log.d("Quiz", "Fetching: $url")

            val httpClient = OkHttpClient()
            val httpRequest = Request.Builder().url(url).build()
            val httpResponse = httpClient.newCall(httpRequest).execute()
            val responseString = httpResponse.body!!.string()
            if (httpResponse.body!!.contentType()!!.subtype != "json") throw RuntimeException(
                "Did not recieve JSON"
            )//TODO: Actual error handling...
            Log.d("Quiz", "Got Json: $responseString")
            return Json.decodeFromString<T>(responseString)
        }

        /**
         * Fetch a new API token from the API
         *
         * @return a String representing the new token
         */
        fun getAPIToken(): String {
            return fetchDecodeJson<APITokenResponse>("https://opentdb.com/api_token.php?command=request").token
        }

        /**
         * Fetch information about the number of quetions per difficulty, in a category
         *
         * @param categoryID the API ID of the category for which to retrieve the summary
         * @return the the parsed json object of the summary
         */
        fun getNumOfQuestions(categoryID: Int): CategoryQuestionSummary {

            return fetchDecodeJson<CategoryQuestionSummary>("https://opentdb.com/api_count.php?category=$categoryID")
        }

        /**
         * gets all the quiz categories in the API
         *
         * @return a list of QuizCategory
         */
        fun getQuizCategories(): List<QuizCategory> {
            val quizCategories = mutableListOf<QuizCategory>()


            for (category in fetchDecodeJson<QuizCategories>("https://opentdb.com/api_category.php").trivia_categories) {

                val quantities =
                    getNumOfQuestions(categoryID = category.id).category_question_count
                val newCat = QuizCategory(
                    category.id, category.name,
                    quantities.total_easy_question_count,
                    quantities.total_medium_question_count,
                    quantities.total_hard_question_count
                )

                quizCategories.add(newCat)
            }

            return quizCategories;
        }

        /**
         * Fetches a list of quustions
         *
         * @param numQuestions number of questions to fetch
         * @param categoryID the id to fetch questions from
         * @param difficulty difficulty of the questions being fetched
         * @param apiToken Optional API-token, provide one to avoid repeating questions across calls
         * @return
         */
        fun getQuestions(
            numQuestions: Int,
            categoryID: Int,
            difficulty: QuizDifficulty,
            apiToken: String? = null
        ): List<QuizQuestion> {

            val endpointUrl: StringBuilder =
                StringBuilder("https://opentdb.com/api.php?amount=$numQuestions&category=$categoryID&difficulty=${difficulty.apiDifficulty}")
            if (apiToken != null) endpointUrl.append("&token=$apiToken")


            val questionCollection =
                fetchDecodeJson<QuestionCollection>(
                    endpointUrl.toString()
                )

            if (questionCollection.response_code == 1)
                throw NoMoreQuestionsException()
            if (questionCollection.response_code != 0)
                throw RuntimeException("Invalid question api result")

            val retVal = mutableListOf<QuizQuestion>()

            for (question in questionCollection.results) {
                retVal.add(
                    QuizQuestion(
                        question.correct_answer,
                        question.incorrect_answers,
                        question.question
                    )
                )
            }
            return retVal
        }


    }
}
