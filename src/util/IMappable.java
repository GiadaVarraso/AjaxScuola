package util;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public interface IMappable {

	default Map<String, String> toMap() {
		Map<String, String> ris = new HashMap<>();

		Class<? extends IMappable> thisClass = getClass();

		Method[] methods = thisClass.getMethods();

		for (Method method : methods) {
			String name = method.getName();

			if (name.startsWith("get") || name.startsWith("is")) {

				try {
					Object v = method.invoke(this);
					String value = v == null ? "" : v.toString();
					name = name.substring(name.startsWith("get") ? 3 : 2);
					ris.put(name, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					continue; // idgaf. Just ignore this field
				}
			}
		}

		ris.put("Class", thisClass.getSimpleName());

		return ris;
	}

	// FIXME metodo da aggiungere per andare ad assegnare le proprietà dell'oggetto
	// a partire da una mappa
	default void fromMap(Map<String, String> map) {
		Method[] methods = getClass().getMethods();

		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				methodName = methodName.substring(3).toLowerCase();
				String value = map.get(methodName);
				if (value == null)
					continue; // Map doesn't have this key. Skip the setter
				// Il tipo del primo paramentro del setter
				try {
					Class<?> parameterType = method.getParameterTypes()[0];
					// FIXME: gestire gli altri tipi
					if (parameterType.equals(int.class)) {
						method.invoke(this, Integer.parseInt(value));
					} else if (parameterType.equals(double.class)) {
						method.invoke(this, Double.parseDouble(value));
					} else if (parameterType.equals(String.class)) {
						method.invoke(this, value);
					}
				} catch (IndexOutOfBoundsException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// Do Nothing. idgaf. Just don't set this value
				}
			}
		}
	}

	static <T extends IMappable> T fromMap(Class<T> type, Map<String, String> map) {
		T ris = null;
	
		try {
			Constructor<T> constructor = type.getConstructor();
			ris = constructor.newInstance();
			ris.fromMap(map);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// Do Nothing. idgaf. Just return null
			System.err.println("Manca il costruttore senza parametri, impossibile istanziare l'oggetto");
		}

		return ris;
	}
}
