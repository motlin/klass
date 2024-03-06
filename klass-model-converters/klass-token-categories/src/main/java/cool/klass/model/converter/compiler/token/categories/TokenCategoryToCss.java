package cool.klass.model.converter.compiler.token.categories;

import com.google.common.base.CaseFormat;

public class TokenCategoryToCss
{
    public static void main(String[] args)
    {
        for (TokenCategory tokenCategory : TokenCategory.values())
        {
            String getCssVar = getCssVar(tokenCategory);
            String result = ""
                    + "        ." + tokenCategory.name() + " {\n"
                    + "            color: " + getCssVar + ";\n"
                    + "        }\n";
            System.out.println(result);
        }
    }

    private static String getCssVar(TokenCategory tokenCategory)
    {
        String tokenNameKebabCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, tokenCategory.name());

        TokenCategory parentCategory = tokenCategory.getParentCategory();
        String        fallbackCssVar = parentCategory == null ? "--color-foreground" : getCssVar(parentCategory);

        return String.format("var(--color-%s, %s)", tokenNameKebabCase, fallbackCssVar);
    }
}
