# Aventura RPG

Mod Fabric para Minecraft 1.21.9 que conserva la supervivencia vanilla y agrega
progresion RPG. La primera version incluye perfiles persistentes, experiencia,
niveles, clases, puntos de atributo y mana.

## Comandos

- `/rpg profile`: muestra tu perfil RPG.
- `/rpg choose <guerrero|mago|arquero|picaro>`: escoge tu clase una sola vez.
- `/rpg admin grantxp <jugador> <cantidad>`: da experiencia; requiere permisos de operador.

## Desarrollo

Requiere JDK 21. Ejecuta `gradlew.bat runClient` para abrir un cliente de desarrollo
y `gradlew.bat build` para generar el JAR en `build/libs`.
