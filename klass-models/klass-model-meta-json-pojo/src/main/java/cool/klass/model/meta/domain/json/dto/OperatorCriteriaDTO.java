package cool.klass.model.meta.domain.json.dto;

public class OperatorCriteriaDTO
        implements CriteriaDTO
{
    private final String             operator;
    private final ExpressionValueDTO sourceExpressionValue;
    private final ExpressionValueDTO targetExpressionValue;

    public OperatorCriteriaDTO(
            String operator,
            ExpressionValueDTO sourceExpressionValue,
            ExpressionValueDTO targetExpressionValue)
    {
        this.operator              = operator;
        this.sourceExpressionValue = sourceExpressionValue;
        this.targetExpressionValue = targetExpressionValue;
    }

    public String getOperator()
    {
        return this.operator;
    }

    public ExpressionValueDTO getSourceExpressionValue()
    {
        return this.sourceExpressionValue;
    }

    public ExpressionValueDTO getTargetExpressionValue()
    {
        return this.targetExpressionValue;
    }
}
