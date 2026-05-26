# AquaGuard - Sistema de Monitoreo y Gestión del Estrés Hídrico
### Equipo: HydroTech Solutions
**Integrantes:** Joaquin Bedon, Ismael Benavides, Juan Diego Coronel  
**Materia:** Programación 3 — Ingeniería de Software  
**Proyecto:** AquaGuard

---

## Estructura del Proyecto

```
AquaGuard/
└── src/main/java/aquaguard/
    ├── model/
    │   ├── FuenteAgua.java       — Entidad: fuente hídrica
    │   ├── Medicion.java         — Entidad: medición de calidad
    │   └── Familia.java          — Entidad: familia beneficiaria
    ├── data/
    │   ├── ArbolAVLFuentes.java  — Árbol AVL (estructura NO lineal)
    │   ├── ColaAlertas.java      — Cola FIFO  (estructura LINEAL)
    │   └── ListaFamilias.java    — Lista Enlazada (estructura LINEAL)
    └── ui/
        ├── VentanaPrincipal.java — JFrame principal con JTabbedPane
        ├── ModuloFuentes.java    — Módulo 1: Fuentes de agua
        ├── ModuloMonitoreo.java  — Módulo 2: Monitoreo y alertas
        └── ModuloDistribucion.java — Módulo 3: Distribución
```

---

## Cómo abrir en IntelliJ IDEA

1. **File → Open** → seleccionar la carpeta `AquaGuard`
2. Crear un nuevo módulo o proyecto con JDK 11 o superior
3. Agregar los archivos `.java` al source root
4. Ejecutar `VentanaPrincipal.java` (tiene el método `main`)

> **Nota sobre archivos .form:**  
> Los archivos `.form` de IntelliJ GUI Designer son XML que se vinculan  
> a clases Java. Para este proyecto se usa Swing puro con código  
> (GridBagLayout, BorderLayout, etc.) porque es más fácil de leer  
> y explicar en la demostración. Si se quiere usar el GUI Designer,  
> crear una nueva clase en IntelliJ con *New → Swing UI Designer → GUI Form*  
> y copiar los componentes del código.

---

## Estructuras de Datos Implementadas

| Módulo | Estructura | Tipo | Operaciones |
|--------|-----------|------|-------------|
| Módulo 1 - Fuentes | **Árbol AVL** | No lineal | Insertar O(log n), Buscar O(log n), Listar O(n) |
| Módulo 2 - Monitoreo | **Cola FIFO** | Lineal | Encolar O(1), Desencolar O(1), Ver frente O(1) |
| Módulo 3 - Distribución | **Lista Enlazada Simple** | Lineal | Agregar O(n), Buscar O(n), Eliminar O(n) |

---

## Módulos del Sistema

### Módulo 1: Gestión de Fuentes de Agua (Joaquin Bedon)
- **CRUD completo:** Registrar, Consultar, Actualizar, Dar de baja (baja lógica)
- **Componentes Swing:** JTextField, JComboBox, JSpinner, JTable
- **Estructura:** Árbol AVL — mantiene fuentes ordenadas por ID con balance automático

### Módulo 2: Monitoreo y Alertas (Ismael Benavides)
- **CRUD completo:** Registrar mediciones, ver historial, atender alertas, corrección
- **Componentes Swing:** JSpinner (pH, turbidez), JCheckBox, JTable, JList
- **Estructura:** Cola FIFO — las alertas se atienden en orden de llegada (FIFO)
- **Lógica:** Clasifica alertas en NORMAL / AMARILLA / NARANJA / ROJA según estándares OMS

### Módulo 3: Distribución y Familias (Juan Diego Coronel)
- **CRUD completo:** Registrar familia, actualizar, eliminar, consultar
- **Componentes Swing:** JTextField, JSpinner, JComboBox, JCheckBox, JTable, JTextArea
- **Estructura:** Lista Enlazada Simple — permite agregar/eliminar familias en cualquier posición
- **Algoritmo:** Distribución equitativa con prioridad para familias vulnerables (reciben cuota doble)

---

## Dependencias
- Java 11+
- Swing (incluido en el JDK, no requiere librerías externas)

---

## Demostración sugerida

1. **Módulo 1:** Registrar una nueva fuente → buscarla en la tabla → actualizarle el nivel → dar de baja
2. **Módulo 2:** Registrar una medición con pH fuera de rango → ver que aparece en la Cola de Alertas → atender alerta (desencolar)
3. **Módulo 3:** Registrar familias (algunas vulnerables) → ingresar litros disponibles → ejecutar distribución y ver resultado del algoritmo
