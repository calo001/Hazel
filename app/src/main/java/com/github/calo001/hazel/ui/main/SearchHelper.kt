package com.github.calo001.hazel.ui.main

import androidx.compose.ui.graphics.Color
import com.github.calo001.hazel.model.hazeldb.*
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.verbs.VerbData
import com.github.calo001.hazel.util.parse

class SearchHelper(private val hazelContent: HazelContent) {
    suspend fun searchQuery(queries: List<String>): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        val queryLowerCase = queries.map { it.lowercase() }

        val colorResults = queryInColors(queryLowerCase, hazelContent.colorHazels)
        val countriesResults = queryInCountries(queryLowerCase, hazelContent.countries)
        val animalsResults = queryInAnimals(queryLowerCase, hazelContent.animals)
        val verbsIrregularResults = queryIrregularVerbs(queryLowerCase, hazelContent.irregularVerbs)
        val verbsRegularResults = queryRegularVerbs(queryLowerCase, hazelContent.regularVerbs)
        val usefulPhrasesResult = queryUsefulPhrases(queryLowerCase, hazelContent.usefulPhrases)

        results.addAll(colorResults)
        results.addAll(countriesResults)
        results.addAll(animalsResults)
        results.addAll(verbsIrregularResults)
        results.addAll(verbsRegularResults)
        results.addAll(usefulPhrasesResult)
        return results.distinctBy { it.route } .toList()
    }

    private fun queryUsefulPhrases(
        queries: List<String>,
        usefulPhrases: List<UsefulPhrase>
    ): List<SearchResult> {
        val resultsUsefulPhrases = mutableListOf<SearchResult>()

        usefulPhrases.forEach { usefulPhrase ->
            if (queries.any { query -> usefulPhrase.category.lowercase().contains(query) }) {
                resultsUsefulPhrases.add(SearchResult(
                    imageCode = usefulPhrase.emojiCode,
                    path = "Useful phrases",
                    itemName = usefulPhrase.category,
                    route = "${Routes.UsefulExpressions.name}/${usefulPhrase.id}"
                ))
            }
            usefulPhrase.phrases.forEach { phrase ->
                if (queries.any { query -> phrase.expression.lowercase().contains(query) }) {
                    resultsUsefulPhrases.add(SearchResult(
                        imageCode = usefulPhrase.emojiCode,
                        path = "Useful phrases / ${usefulPhrase.category}",
                        itemName = phrase.expression,
                        route = "${Routes.UsefulExpressionsPhrase.name}/${usefulPhrase.id}/${phrase.id}"
                    ))
                }
            }
        }

        if (queries.any { query ->
                "useful".contains(query) ||
                "phrases".contains(query) ||
                "useful phrases".contains(query)
            }
        ) {
            usefulPhrases.forEach { usefulPhrase ->
                resultsUsefulPhrases.add(SearchResult(
                    imageCode = usefulPhrase.emojiCode,
                    path = "Useful phrases",
                    itemName = usefulPhrase.category,
                    route = "${Routes.UsefulExpressions.name}/${usefulPhrase.id}"
                ))
            }
        }

        return resultsUsefulPhrases
    }

    private suspend fun queryInAnimals(queries: List<String>, animals: List<Animal>): List<SearchResult> {
        val resultsAnimals = mutableListOf<SearchResult>()

        if (queries.any { query -> "animal".contains(query) or "animals".contains(query) }) {
            resultsAnimals.add(SearchResult(
                imageCode = "openmoji_1f638",
                path = "",
                itemName = "Animals",
                route = Routes.Animals.name
            ))
        }

        animals.forEach { animal ->
            if (queries.any { query -> animal.name.lowercase().contains(query) }) {
                resultsAnimals.add(SearchResult(
                    imageCode = animal.emojiCode,
                    path = "Animals",
                    itemName = animal.name,
                    route = "${Routes.Animals.name}/${animal.id}"
                ))
            }
        }

        return resultsAnimals.toList()
    }

    private suspend fun queryIrregularVerbs(
        queries: List<String>,
        verbs: List<Verb>,
    ): List<SearchResult> {
        val resultsVerbs = mutableListOf<SearchResult>()

        if (queries.any { query ->
                "verb".contains(query) or "verbs".contains(query)or "irregular verbs".contains(query) or "irregular verb".contains(query)
            }) {
            resultsVerbs.add(SearchResult(
                imageCode = "openmoji_1f93e_200d_2640_fe0f",
                path = "",
                itemName = "Irregular verbs",
                route = Routes.VerbsIrregular.name
            ))
        }

        verbs.forEach { verb ->
            if (queries.any { query -> verb.base.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Base form",
                    itemName = verb.base.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.BaseForm.name}"
                ))
            }

            if (queries.any { query -> verb.simplePast.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Simple past form",
                    itemName = verb.simplePast.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.PastForm.name}"
                ))
            }

            if (queries.any { query -> verb.pastParticiple.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Past participle form",
                    itemName = verb.pastParticiple.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.PastParticipleForm.name}"
                ))
            }

            if (queries.any { query -> verb.ing.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Ing form",
                    itemName = verb.ing.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.IngForm.name}"
                ))
            }
        }

        return resultsVerbs.toList()
    }

    private suspend fun queryRegularVerbs(
        queries: List<String>,
        verbs: List<Verb>,
    ): List<SearchResult> {
        val resultsVerbs = mutableListOf<SearchResult>()

        if (queries.any { query ->
                "verb".contains(query) or
                "verbs".contains(query) or
                "regular verbs".contains(query) or
                "regular verb".contains(query) }
        ) {
            resultsVerbs.add(SearchResult(
                imageCode = "openmoji_1f939_200d_2642_fe0f",
                path = "",
                itemName = "Regular verbs",
                route = Routes.VerbsRegular.name
            ))
        }

        verbs.forEach { verb ->
            if (queries.any { query -> verb.base.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Base form",
                    itemName = verb.base.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.BaseForm.name}"
                ))
            }

            if (queries.any { query -> verb.simplePast.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Simple past form",
                    itemName = verb.simplePast.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.PastForm.name}"
                ))
            }

            if (queries.any { query -> verb.pastParticiple.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Past participle form",
                    itemName = verb.pastParticiple.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.PastParticipleForm.name}"
                ))
            }

            if (queries.any { query -> verb.ing.verb.lowercase().contains(query) }) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Ing form",
                    itemName = verb.ing.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.IngForm.name}"
                ))
            }
        }

        return resultsVerbs.toList()
    }

    private suspend fun queryInCountries(queries: List<String>, countries: List<Country>): List<SearchResult> {
        val resultsCountries = mutableListOf<SearchResult>()

        if (queries.any { query -> "country".contains(query) or "countries".contains(query) }) {
            resultsCountries.add(SearchResult(
                imageCode = "openmoji_1f5fa",
                path = "",
                itemName = "Countries",
                route = Routes.Countries.name
            ))
        }

        countries.forEach { country ->
            if (queries.any { query -> country.name.lowercase().contains(query) }) {
                resultsCountries.add(SearchResult(
                    imageCode = country.emojiCode,
                    path = "Countries",
                    itemName = country.name,
                    route = "${Routes.Countries.name}/${country.id}"
                ))
            }
        }

        return resultsCountries.toList()
    }

    private suspend fun queryInColors(queries: List<String>, colorHazels: List<ColorHazel>): List<SearchResult> {
        val resultsColors = mutableListOf<SearchResult>()

        if (queries.any { query -> "color".contains(query) or "colors".contains(query)}) {
            resultsColors.add(SearchResult(
                imageCode = "openmoji_2b21_fe0f_200d_1f308",
                path = "",
                itemName = "Colors",
                route = Routes.Colors.name
            ))
        }

        colorHazels.forEach { color ->
            if (queries.any { query -> color.name.lowercase().contains(query) }) {
                resultsColors.add(SearchResult(
                    color = Color.parse(color.code),
                    path = "Colors",
                    itemName = color.name,
                    route = "${Routes.Colors.name}/${color.id}"
                ))
            }
        }

        return resultsColors.toList()
    }
}

class SearchResult(
    val imageCode: String? = null,
    val color: Color? = null,
    val path: String,
    val itemName: String,
    val route: String,
)