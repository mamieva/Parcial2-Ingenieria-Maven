# Decisiones de diseño para las pruebas unitarias

Para poder realizar las pruebas unitarias de manera efectiva, se agregaron
algunos atributos y consideraciones que el enunciado no mencionaba
explícitamente pero que son indispensables. Todas estas adiciones son **no
intrusivas**: ningún atributo nuevo cambia el comportamiento del dominio
descrito en el UML, solo lo hacen *observable* para poder testearlo.

## 1. `List<Luz> luces` + `getLuces()` en `Semaforo`

El enunciado dice que el semáforo se compone de 3 luces, pero no expone la
colección. Para verificar la composición con `assertEquals(3, sem.getLuces().size())`
y los colores (`get(0).getColor()` == "Rojo", etc.) se requiere la lista interna
y su getter de solo lectura.

## 2. `int cantidadReparaciones` + `registrarReparacion()` (package-private) en `Semaforo`

El UML pide `getCantidadTotalReparaciones()`, pero ese getter necesita un
contador detrás. `registrarReparacion()` lo incrementa y lo llama la
`OrdenDeComposicion` al completar. Es lo que permite el assert
`assertEquals(1, sem.getCantidadTotalReparaciones())`.

## 3. `List<Denuncia> historialDenuncias` + `getHistorialDenuncias()` en `Semaforo`

Para cumplir con el requerimiento de "llevar la cuenta de cuántas
reparaciones/denuncias se han hecho a un semáforo a lo largo del tiempo", el
semáforo (o el servicio) debe guardar este historial. Se incluyó en el propio
`Semaforo` para facilitar el testeo directo: la asociación `Semaforo ↔ Denuncia`
ya existe en el UML, y el getter del historial permite afirmar el enlace
bidireccional con `assertEquals(1, sem.getHistorialDenuncias().size())`.

## 4. Registro estático `TODAS` + `reiniciarRegistro()` en `Denuncia`

`listDenunciaPorSemaforo` y `listDenunciaPorDenunciante` necesitan buscar entre
*todas* las denuncias del sistema, no solo las de una instancia. Se resuelve con
una lista estática. `reiniciarRegistro()` (package-private) limpia ese estado
global en el `@BeforeEach` de los tests para que una prueba no contamine a la
otra.

## 5. `boolean estaLibre` + getter `isEstaLibre()` en `Miembro`

El UML solo declara `setEstaLibre(boolean)`. Se agrega el getter `isEstaLibre()`
para poder verificar con `assertTrue` que el personal queda disponible tras
finalizar el trabajo (y ocupado mientras dura), porque para testearlo hace falta
poder *leer* el estado, no solo escribirlo
(`miembros.stream().allMatch(Miembro::isEstaLibre)` /
`noneMatch(...)`). Además el atributo `estaLibre` nace en `true`: un miembro
recién creado está disponible.

## 6. Getters de lectura adicionales

`getResponsable()`, `getMiembros()` en `EquipoDeControl`; `getMisDenuncias()` en
`Denunciante`; `getEquipoAsignado()`, `getDenunciaAsociada()`,
`getFechaEfectivaReparacion()` en `OrdenDeComposicion`; entre otros.

No agregan comportamiento; son ventanas de solo lectura sobre el estado interno,
indispensables para que los tests puedan hacer las aserciones (p. ej. confirmar
que la orden quedó ligada a su denuncia, o que el responsable es el del índice
indicado).

## 7. Constantes `LIBRE` / `OCUPADO` en `EquipoDeControl`

El estado se modela como `String`. Definirlos como constantes evita errores de
tipeo y deja los asserts (`assertEquals("OCUPADO", equipo.getEstado())`)
alineados con la implementación.

## 8. `OrdenDeComposicion ordenAsignada` en `Denuncia` + `OrdenYaAsignadaException`

Para poder evaluar mediante `assertThrows` si una denuncia ya tiene una orden
asignada, se agregó el enlace inverso `ordenAsignada` en `Denuncia` (la relación
`Denuncia "1" <-- "1" OrdenDeComposicion` del UML). La orden nace referenciando su
denuncia, pero el enlace inverso (denuncia → orden) lo realiza de forma explícita
el servicio mediante `GestionSemaforosService.asignarOrden(denuncia, orden)`, que
delega en `denuncia.asignarOrden(orden)`. Si la denuncia ya tenía una orden, se
lanza `OrdenYaAsignadaException`, respetando la multiplicidad "1" (una denuncia se
liga a una única orden).

Se concentró la asignación en el servicio (en vez de hacerla en el constructor de
la orden) para no duplicar la lógica de enlace y para que el test
`testNoSeAsignaSegundaOrden` pueda verificar con
`assertThrows(OrdenYaAsignadaException.class, ...)` —y dentro de un
`@Timeout(400 ms)`— que `service.asignarOrden(...)` falla al asociar una segunda
orden a la misma denuncia.
