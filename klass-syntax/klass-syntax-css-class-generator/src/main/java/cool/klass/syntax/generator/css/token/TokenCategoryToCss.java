package cool.klass.syntax.generator.css.token;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;

public final class TokenCategoryToCss
{
    public static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_HYPHEN);

    private TokenCategoryToCss()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    // TODO: turn this into a plugin and run it inside klass-syntax-css-class
    public static void main(String[] args)
    {
        for (TokenCategory tokenCategory : TokenCategory.values())
        {
            //language=CSS
            String result = ""
                    + ".klass-" + TokenCategoryToCss.getTokenCategoryName(tokenCategory) + " {\n"
                    + "    color: " + TokenCategoryToCss.getCssVar(tokenCategory) + ";\n"
                    + "}\n";
            System.out.print(result);
        }
    }

    private static String getCssVar(TokenCategory tokenCategory)
    {
        TokenCategory parentCategory = tokenCategory.getParentCategory();
        String fallbackCssVar = parentCategory == null ? "--color-foreground" : TokenCategoryToCss.getCssVar(
                parentCategory);

        return String.format(
                "var(--klass-color-%s, %s)",
                TokenCategoryToCss.getTokenCategoryName(tokenCategory),
                fallbackCssVar);
    }

    private static String getTokenCategoryName(TokenCategory tokenCategory)
    {
        return CONVERTER.convert(tokenCategory.name());
    }
}
