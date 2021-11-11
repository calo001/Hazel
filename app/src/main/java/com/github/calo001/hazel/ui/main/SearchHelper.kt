package com.github.calo001.hazel.ui.main

import androidx.compose.ui.graphics.Color
import com.github.calo001.hazel.model.hazeldb.*
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.verbs.VerbData
import com.github.calo001.hazel.util.parse

class SearchHelper(private val hazelContent: HazelContent) {
    suspend fun searchQuery(query: String): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        val queryLowerCase = query.lowercase()

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
        return results.toList()
    }

    private fun queryUsefulPhrases(
        query: String,
        usefulPhrases: List<UsefulPhrase>
    ): List<SearchResult> {
        val resultsUsefulPhrases = mutableListOf<SearchResult>()

        usefulPhrases.forEach { usefulPhrase ->
            if (usefulPhrase.category.lowercase().contains(query)) {
                resultsUsefulPhrases.add(SearchResult(
                    imageCode = usefulPhrase.emojiCode,
                    path = "Useful phrases",
                    itemName = usefulPhrase.category,
                    route = "${Routes.UsefulExpressions.name}/${usefulPhrase.category}"
                ))
            }
            usefulPhrase.phrases.forEach { phrase ->
                if (phrase.expression.lowercase().contains(query)) {
                    resultsUsefulPhrases.add(SearchResult(
                        imageCode = usefulPhrase.emojiCode,
                        path = "Useful phrases / ${usefulPhrase.category}",
                        itemName = phrase.expression,
                        route = "${Routes.UsefulExpressionsPhrase.name}/${usefulPhrase.category}/${phrase.expression}"
                    ))
                }
            }
        }

        if ("useful".contains(query)
            or "phrases".contains(query)
            or "useful phrases".contains(query)
        ) {
            usefulPhrases.forEach { usefulPhrase ->
                resultsUsefulPhrases.add(SearchResult(
                    imageCode = usefulPhrase.emojiCode,
                    path = "Useful phrases",
                    itemName = usefulPhrase.category,
                    route = "${Routes.UsefulExpressions.name}/${usefulPhrase.category}"
                ))
            }
        }

        return resultsUsefulPhrases
    }

    private suspend fun queryInAnimals(query: String, animals: List<Animal>): List<SearchResult> {
        val resultsAnimals = mutableListOf<SearchResult>()

        if ("animal".contains(query) or "animals".contains(query)) {
            resultsAnimals.add(SearchResult(
                imageCode = "openmoji_1f638",
                path = "",
                itemName = "Animals",
                route = Routes.Animals.name
            ))
        }

        animals.forEach { animal ->
            if (animal.name.lowercase().contains(query)) {
                resultsAnimals.add(SearchResult(
                    imageCode = animal.emojiCode,
                    path = "Animals",
                    itemName = animal.name,
                    route = "${Routes.Animals.name}/${animal.name}"
                ))
            }
        }

        return resultsAnimals.toList()
    }

    private suspend fun queryIrregularVerbs(
        query: String,
        verbs: List<Verb>,
    ): List<SearchResult> {
        val resultsVerbs = mutableListOf<SearchResult>()

        if ("verb".contains(query)
            or "verbs".contains(query)
            or "irregular verbs".contains(query)
            or "irregular verb".contains(query)
        ) {
            resultsVerbs.add(SearchResult(
                imageCode = "openmoji_1f93e_200d_2640_fe0f",
                path = "",
                itemName = "Irregular verbs",
                route = Routes.VerbsIrregular.name
            ))
        }

        verbs.forEach { verb ->
            if (verb.base.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Base form",
                    itemName = verb.base.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.base.verb}/${VerbData.BaseForm.name}"
                ))
            }

            if (verb.simplePast.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Simple past form",
                    itemName = verb.simplePast.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.base.verb}/${VerbData.PastForm.name}"
                ))
            }

            if (verb.pastParticiple.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Past participle form",
                    itemName = verb.pastParticiple.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.base.verb}/${VerbData.PastParticipleForm.name}"
                ))
            }

            if (verb.ing.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Irregular verbs / Ing form",
                    itemName = verb.ing.verb,
                    route = "${Routes.VerbsIrregular.name}/${verb.base.verb}/${VerbData.IngForm.name}"
                ))
            }
        }

        return resultsVerbs.toList()
    }

    private suspend fun queryRegularVerbs(
        query: String,
        verbs: List<Verb>,
    ): List<SearchResult> {
        val resultsVerbs = mutableListOf<SearchResult>()

        if ("verb".contains(query)
            or "verbs".contains(query)
            or "regular verbs".contains(query)
            or "regular verb".contains(query)
        ) {
            resultsVerbs.add(SearchResult(
                imageCode = "openmoji_1f939_200d_2642_fe0f",
                path = "",
                itemName = "Regular verbs",
                route = Routes.VerbsRegular.name
            ))
        }

        verbs.forEach { verb ->
            if (verb.base.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Base form",
                    itemName = verb.base.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.base.verb}/${VerbData.BaseForm.name}"
                ))
            }

            if (verb.simplePast.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Simple past form",
                    itemName = verb.simplePast.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.base.verb}/${VerbData.PastForm.name}"
                ))
            }

            if (verb.pastParticiple.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Past participle form",
                    itemName = verb.pastParticiple.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.base.verb}/${VerbData.PastParticipleForm.name}"
                ))
            }

            if (verb.ing.verb.lowercase().contains(query)) {
                resultsVerbs.add(SearchResult(
                    imageCode = verb.emojiCode,
                    path = "Regular verbs / Ing form",
                    itemName = verb.ing.verb,
                    route = "${Routes.VerbsRegular.name}/${verb.base.verb}/${VerbData.IngForm.name}"
                ))
            }
        }

        return resultsVerbs.toList()
    }

    private suspend fun queryInCountries(query: String, countries: List<Country>): List<SearchResult> {
        val resultsCountries = mutableListOf<SearchResult>()

        if ("country".contains(query) or "countries".contains(query)) {
            resultsCountries.add(SearchResult(
                imageCode = "openmoji_1f5fa",
                path = "",
                itemName = "Countries",
                route = Routes.Countries.name
            ))
        }

        countries.forEach { country ->
            if (country.name.lowercase().contains(query)) {
                resultsCountries.add(SearchResult(
                    imageCode = country.emojiCode,
                    path = "Countries",
                    itemName = country.name,
                    route = "${Routes.Countries.name}/${country.name}"
                ))
            }
        }

        return resultsCountries.toList()
    }

    private suspend fun queryInColors(query: String, colorHazels: List<ColorHazel>): List<SearchResult> {
        val resultsColors = mutableListOf<SearchResult>()

        if ("color".contains(query) or "colors".contains(query)) {
            resultsColors.add(SearchResult(
                imageCode = "openmoji_2b21_fe0f_200d_1f308",
                path = "",
                itemName = "Colors",
                route = Routes.Colors.name
            ))
        }

        colorHazels.forEach { color ->
            if (color.name.lowercase().contains(query)) {
                resultsColors.add(SearchResult(
                    color = Color.parse(color.code),
                    path = "Colors",
                    itemName = color.name,
                    route = "${Routes.Colors.name}/${color.code}"
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