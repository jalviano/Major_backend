package triangle;

import java.lang.reflect.Array;

public class Triangle {

	public static enum Type {
		INVALID, SCALENE, EQUILATERAL, ISOSCELES
	};
	
	public static Type classify(int a, int b, int c) {
		int trian;
		if (a <= 0 || b <= 0 || c <= 0)
			return Type.INVALID;
		trian = 0;
		if (a == b)
			trian = trian + 1;
		if (a == c)
			trian = trian + 2;
		if (b == c)
			trian = trian + 3;
		if (trian == 0)
			if (a + b <= c || a + c <= b || b + c <= a)
				return Type.INVALID;
			else
				return Type.SCALENE;
		if (trian > 3)
			return Type.EQUILATERAL;
		if (trian == 1 && a + b > c)
			return Type.ISOSCELES;
		else if (trian == 2 && a + c > b)
			return Type.ISOSCELES;
		else if (trian == 3 && b + c > a)
			return Type.ISOSCELES;
		return Type.INVALID;
	}

	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	public static Object[] nullToEmpty(final Object[] array) {
		if (isEmpty(array)) {
			return EMPTY_OBJECT_ARRAY;
		}
		return array;
	}

	public static boolean isEmpty(final Object[] array) {
		return getLength(array) == 0;
	}

	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	public static boolean[] add(final boolean[] array, final boolean element) {
		final boolean[] newArray = (boolean[])copyArrayGrow1(array, Boolean.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
		if (array != null) {
			final int arrayLength = Array.getLength(array);
			final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}

	/*public static void runForever() {
	    int i = 0;
		while (i < 4) {
			i += 1;
			// System.out.println(Thread.currentThread().getName() + ": " + i);
		}
	}*/
}
