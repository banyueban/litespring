package org.litespring.aop.aspectj;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.reflect.ReflectionWorld.ReflectionWorldException;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;
import org.litespring.aop.MethodMatcher;
import org.litespring.aop.Pointcut;
import org.litespring.util.ClassUtils;
import org.litespring.util.StringUtils;

/**
 * @author MAQUN
 * @version V1.0 
 * @Date 2019-04-17 15:32:34
 * 匹配目标Pointcut
 */
public class AspectJExpressionPointcut implements MethodMatcher, Pointcut {

	private String expression;
	
	private PointcutExpression pointcutExpression;
	
	private ClassLoader pointcutClassLoader;
	
	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();
	
	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
	}
	
	public AspectJExpressionPointcut() {
		
	}
	
	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}

	@Override
	public String getExpression() {
		return this.expression;
	}

	@Override
	public boolean matches(Method method/*, Class<?> targetClass*/) {
		checkReadyToMatch();
		ShadowMatch shadowMatch = getShadowMatch(method);
		if (shadowMatch.alwaysMatches()) {
			return true;
		}
		return false;
	}

	private ShadowMatch getShadowMatch(Method method) {
		ShadowMatch shadowMatch = null;
		try {
			// 不要调错方法,这里是匹配Method
			shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
			
		} catch(ReflectionWorldException ex) {
			throw new RuntimeException("not implemented yet");
		}
		return shadowMatch;
	}

	private void checkReadyToMatch() {
		if (getExpression() == null) {
			throw new IllegalArgumentException("Must set property 'expression' before attemping to match");
		}
		if (null == this.pointcutExpression) {
			this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
			this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
		}
	}

	private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
		PointcutParser parser = PointcutParser
				.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, classLoader);
		return parser.parsePointcutExpression(replaceBooleanOperators(getExpression()), null, new PointcutParameter[0]);
	}

	private String replaceBooleanOperators(String pcExpr) {
		String result = StringUtils.replace(pcExpr, " and ", " && ");
		result = StringUtils.replace(result, " or ", " || ");
		result = StringUtils.replace(result, " not ", " ! ");
		return result;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
