package com.github.calo001.hazel.model.widget

import com.github.calo001.hazel.model.hazeldb.HazelContent
import com.github.calo001.hazel.routes.Routes
import com.github.calo001.hazel.ui.verbs.VerbData
import com.github.calo001.hazel.ui.widget.*

data class HazelSimpleItem(
    val name: String,
    val image: String? = null,
    val color: String? = null,
    val type: String,
    val route: String = Routes.Colors.name,
)

const val COUNTRY = "country"
const val ANIMALS = "animals"
const val COLOR = "colors"
const val REGULAR_VERBS = "regular_verbs"
const val IRREGULAR_VERBS = "irregular_verbs"

fun HazelContent.getSimpleItemList(typeContent: String): List<HazelSimpleItem> {
    val result = mutableListOf<HazelSimpleItem>()

    when(typeContent) {
        Routes.Colors.name -> {
            this.colorHazels.forEach { colorHazel ->
                result.add(HazelSimpleItem(
                    name = colorHazel.name,
                    color = colorHazel.code,
                    type = COLOR,
                    route = "${Routes.Colors.name}/${colorHazel.id}"
                ))
            }
        }
        Routes.VerbsIrregular.name -> {
            this.regularVerbs.forEach { verb ->
                result.add(HazelSimpleItem(
                    name = verb.base.verb,
                    image = verb.emojiCode,
                    type = REGULAR_VERBS,
                    route = "${Routes.VerbsRegular.name}/${verb.id}/${VerbData.BaseForm.name}"
                ))
            }
        }
        Routes.VerbsRegular.name -> {
            this.irregularVerbs.forEach { verb ->
                result.add(HazelSimpleItem(
                    name = verb.base.verb,
                    image = verb.emojiCode,
                    type = IRREGULAR_VERBS,
                    route = "${Routes.VerbsIrregular.name}/${verb.id}/${VerbData.BaseForm.name}"
                ))
            }
        }
        Routes.Countries.name -> {
            this.countries.forEach { country ->
                result.add(HazelSimpleItem(
                    name = country.name,
                    image = country.emojiCode,
                    type = COUNTRY,
                    route = "${Routes.Countries.name}/${country.id}"
                ))
            }
        }
        Routes.Animals.name -> {
            this.animals.forEach { animal ->
                result.add(HazelSimpleItem(
                    name = animal.name,
                    image = animal.emojiCode,
                    type = ANIMALS,
                    route = "${Routes.Animals.name}/${animal.id}"
                ))
            }
        }
    }

    return result
}