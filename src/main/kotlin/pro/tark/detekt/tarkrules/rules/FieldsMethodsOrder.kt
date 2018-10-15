package pro.tark.detekt.tarkrules.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.psi.psiUtil.isProtected
import org.jetbrains.kotlin.psi.psiUtil.isPublic


/**
 * This is a sample rule reporting too many functions inside a file.
 *
 * @author Artur Bosch
 * @author Marvin Ramin
 */
class FieldsMethodsOrder : Rule() {

    override val issue = Issue(javaClass.simpleName,
            Severity.Defect,
            "This rule reports a file with incorrect fields and methods order.",
            Debt.FIVE_MINS)

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)
        checkFields(classOrObject);
        checkFunctions(classOrObject)
        checkFunctionsAfterProperties(classOrObject)
    }

    private fun checkFunctionsAfterProperties(klassOrObject: KtClassOrObject) {
        val fields = klassOrObject
                .declarations
                .asSequence()
                .filter { it is KtNamedFunction || it is KtProperty }
                .toList()
                .toTypedArray()

        val sortedList =
                fields.copyOf().apply {
                    sortWith(
                            compareByDescending<Any>
                            { it is KtProperty }
                                    .thenByDescending { it is KtNamedFunction }
                    )
                }
        val equals = fields.contentEquals(sortedList);

        if (!equals) {
            report(CodeSmell(issue,
                    Entity.from(klassOrObject), "Functions should be after properties"
            ))
        }
    }


    private fun checkFields(klassOrObject: KtClassOrObject) {
        val fields = klassOrObject
                .declarations
                .asSequence()
                .filterIsInstance<KtProperty>()
                .filter {
                    it.isPublic || it.isProtected() || it.isPrivate()
                }
                .toList()
                .toTypedArray()

        val sortedList =
                fields.copyOf().apply {
                    sortWith(
                            compareByDescending<KtProperty>
                            { it.isPublic }
                                    .thenByDescending { it.isProtected() }
                                    .thenByDescending { it.isPrivate() })
                }

        val equals = fields.contentEquals(sortedList);

        if (!equals) {
            report(CodeSmell(issue,
                    Entity.from(klassOrObject), "Invalid order for fields"
            ))
        }

    }


    private fun checkFunctions(klassOrObject: KtClassOrObject) {
        val functionList = klassOrObject
                .declarations
                .asSequence()
                .filterIsInstance<KtNamedFunction>()
                .filter {
                    (it.isPublic || it.isProtected() || it.isPrivate()) &&
                            !isFunctionOverriden(it) && !isFunctionAbstract(it)
                }
                .toList()
                .toTypedArray()


        val sortedList =
                functionList.copyOf().apply {
                    sortWith(compareByDescending<KtNamedFunction> { it.isPublic }
                            .thenByDescending { it.isProtected() }
                            .thenByDescending { it.isPrivate() })
                }


        val equals = functionList.contentEquals(sortedList);

        if (!equals) {
            report(CodeSmell(issue,
                    Entity.from(klassOrObject), "Invalid order for functions"
            ))
        }

    }


    private fun isFunctionOverriden(function: KtNamedFunction): Boolean {
        return function.hasModifier(KtTokens.OVERRIDE_KEYWORD);
    }


    private fun isFunctionAbstract(function: KtNamedFunction): Boolean {
        return function.hasModifier(KtTokens.ABSTRACT_KEYWORD);
    }


}
