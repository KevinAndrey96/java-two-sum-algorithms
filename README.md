# Two Sum en Java: fuerza bruta, dos punteros y tabla hash (Java 17+)

[![CI](https://github.com/KevinAndrey96/java-two-sum-algorithms/actions/workflows/ci.yml/badge.svg)](https://github.com/KevinAndrey96/java-two-sum-algorithms/actions/workflows/ci.yml)
![Java 17 | 21 | 25](https://img.shields.io/badge/Java-17%20%7C%2021%20%7C%2025-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-6-25A162?logo=junit5&logoColor=white)
[![Licencia MIT](https://img.shields.io/badge/licencia-MIT-green)](LICENSE)

Tres soluciones en **Java** al problema **Two Sum**: dado un arreglo de enteros y un objetivo $X$, encontrar dos posiciones distintas $i < j$ con $a_i + a_j = X$. Las tres estrategias comparten una interfaz, se verifican entre sí con miles de arreglos aleatorios y suman en `long`, así que no fallan con valores cercanos a `Integer.MAX_VALUE`.

## Contenido

- [Enunciado](#enunciado)
- [Uso](#uso)
- [Estrategias](#estrategias)
- [Desbordamiento](#desbordamiento)
- [Defectos de la versión de 2019 corregidos](#defectos-de-la-versión-de-2019-corregidos)
- [Estructura](#estructura)
- [Pruebas y calidad](#pruebas-y-calidad)
- [Historia del proyecto](#historia-del-proyecto)

## Enunciado

> Dado un arreglo de enteros y un valor $X$, encontrar dos índices $i$ y $j$ tales que la suma de sus elementos sea $X$, o indicar que no existen.

## Uso

```bash
mvn -B verify
java -jar target/two-sum.jar 9 2 7 11 15
```

```text
Arreglo  : [2, 7, 11, 15]
Objetivo : 9
Fuerza bruta                 [O(n²) tiempo, O(1) memoria]         índices 0 y 1 (2 + 7)
Ordenamiento y dos punteros  [O(n log n) tiempo, O(n) memoria]    índices 0 y 1 (2 + 7)
Tabla hash                   [O(n) tiempo esperado, O(n) memoria] índices 0 y 1 (2 + 7)
```

Como el ejercicio original, también genera arreglos aleatorios con valores de 1 a 99. Con una semilla el resultado es reproducible:

```bash
java -jar target/two-sum.jar --aleatorio 10 100 42
```

```text
Arreglo  : [63, 49, 64, 72, 37, 53, 24, 3, 44, 84]
Objetivo : 100
Fuerza bruta                 [O(n²) tiempo, O(1) memoria]         índices 0 y 4 (63 + 37)
Ordenamiento y dos punteros  [O(n log n) tiempo, O(n) memoria]    índices 0 y 4 (63 + 37)
Tabla hash                   [O(n) tiempo esperado, O(n) memoria] índices 0 y 4 (63 + 37)
```

Códigos de salida: `0` si hay solución, `1` si no la hay y `2` ante argumentos inválidos, con un mensaje en español.

## Estrategias

| Estrategia | Tiempo | Memoria extra | Par que devuelve |
| --- | --- | --- | --- |
| `BruteForceTwoSum` | $O(n^2)$ | $O(1)$ | El menor en orden lexicográfico $(i, j)$ |
| `TwoPointersTwoSum` | $O(n \log n)$ | $O(n)$ | Uno cualquiera |
| `HashMapTwoSum` | $O(n)$ esperado | $O(n)$ | El de menor $j$ |

**Fuerza bruta.** Revisa los $\binom{n}{2}$ pares con $i < j$. Sirve como referencia para las pruebas.

**Ordenamiento y dos punteros.** Ordena las posiciones por valor y coloca un puntero en el menor y otro en el mayor. Si $a_{lo} + a_{hi} < X$, el menor no puede formar parte de ninguna solución, porque su mejor pareja posible ya es el mayor, y se descarta; si la suma es mayor que $X$, se descarta el mayor por el argumento simétrico. El arreglo de entrada no se modifica: se ordena una copia donde cada `long` lleva el valor en los 32 bits altos y la posición en los bajos, así que un solo `Arrays.sort` ordena por valor sin crear objetos.

**Tabla hash.** Recorre el arreglo una vez y, para cada $a_j$, busca si ya vio el complemento $X - a_j$. Guarda solo la primera posición de cada valor, lo que basta para responder y conserva el par que se completa primero.

## Desbordamiento

En `int`, `Integer.MAX_VALUE + 1` da `Integer.MIN_VALUE`. Un Two Sum que suma en `int` responde que `[2147483647, 1]` suma `-2147483648`. Aquí las sumas y el objetivo son `long`, y las pruebas cubren ese caso y el de dos `Integer.MIN_VALUE`.

## Defectos de la versión de 2019 corregidos

| Defecto original | Consecuencia | Corrección |
| --- | --- | --- |
| Los dos ciclos iban de 0 a 9 sin exigir $i \ne j$ | Con $X = 2a_i$ usaba el mismo elemento dos veces | Pares con $i < j$ en el registro `IndexPair` |
| El `break` solo salía del ciclo interno | Seguía buscando y reportaba el par del último $i$ con solución, no el primero | Retorno inmediato con `Optional` |
| Tamaño fijo de 10 y ventanas `JOptionPane` | No se podía probar ni reutilizar | Arreglos de cualquier tamaño, CLI y pruebas |
| Sumas en `int` | Falsos positivos por desbordamiento | Aritmética en `long` |

## Estructura

| Clase | Responsabilidad |
| --- | --- |
| `TwoSum` | Interfaz sellada común a las tres estrategias |
| `BruteForceTwoSum`, `TwoPointersTwoSum`, `HashMapTwoSum` | Estrategias |
| `IndexPair` | Par de posiciones distintas y ordenadas (`record`) |
| `cli.TwoSumCli` | Argumentos, arreglos aleatorios y salida en español |

## Pruebas y calidad

```bash
mvn -B verify   # compila con -Xlint:all -Werror y ejecuta 43 pruebas JUnit
```

- Cada estrategia pasa los mismos casos: ejemplo clásico, posiciones repetidas, valores negativos, desbordamiento y arreglo sin modificar.
- Las tres se comparan con la fuerza bruta en 3.000 arreglos aleatorios y se verifica que el par devuelto sume el objetivo.
- La CLI se prueba con semillas fijas y con cada tipo de argumento inválido.

GitHub Actions compila y prueba en Java 17, 21 y 25, y ejecuta una prueba de humo del JAR con 20.000 valores.

## Historia del proyecto

Ejercicio de septiembre de 2019 del curso de algoritmos, hecho en NetBeans con ventanas `JOptionPane`. El código original se conserva en la etiqueta [`v0.1-java-coursework-2019`](https://github.com/KevinAndrey96/java-two-sum-algorithms/tree/v0.1-java-coursework-2019); la carpeta de compilación y la configuración privada de NetBeans se purgaron del historial. En 2026 se reescribió con tres estrategias, pruebas, Maven y CI.

## Autor

**Kevin Andrey Herrera Silva** · [GitHub](https://github.com/KevinAndrey96)

## Licencia

[MIT](LICENSE) © 2019-2026 Kevin Andrey Herrera Silva
