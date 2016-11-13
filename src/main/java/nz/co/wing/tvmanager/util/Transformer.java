package nz.co.wing.tvmanager.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class Transformer<T, F> {

	@SuppressWarnings("unchecked")
	public T transform(final F from) {
		try {
			final ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
			final Class<T> type = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
			final T to = type.newInstance();
			final Field[] fields = to.getClass().getDeclaredFields();
			for (final Field field : fields) {
				field.setAccessible(true);
				try {
					final Field fromField = from.getClass().getDeclaredField(field.getName());
					fromField.setAccessible(true);
					final Object value = fromField.get(from);
					field.set(to, value);
				} catch (final NoSuchFieldException nsfe) {
					// ignore
				}
			}
			return to;
		} catch (InstantiationException | IllegalAccessException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
