package pro.tark.detekt.tarkrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import pro.tark.detekt.tarkrules.rules.FieldsMethodsOrder

class TarkRulesProvider : RuleSetProvider {

    override val ruleSetId: String = "tark"

    override fun instance(config: Config): RuleSet {
        return RuleSet(ruleSetId, listOf(FieldsMethodsOrder()))
    }

}