def call(Map config = [:]) {

    // 1) Parámetro: abortPipeline (por defecto false)
    boolean abortPipeline = config.get('abortPipeline', false) as boolean

    // 2) Rama: leer variable de entorno de Jenkins (BRANCH_NAME)
    // Si no existe (pipeline normal), usar el parámetro branchName como respaldo.
    String branchName = (env.BRANCH_NAME ?: config.get('branchName', 'unknown')).toString()

    // 3) Heurística de corte
    boolean shouldAbort = false

    if (abortPipeline) {
        shouldAbort = true
    } else if (branchName == "master") {
        shouldAbort = true
    } else if (branchName.startsWith("hotfix")) {
        shouldAbort = true
    }

    // 4) Simulación de análisis estático (sin SonarQube real)
    timeout(time: 5, unit: 'MINUTES') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'

        // Simulamos resultado del Quality Gate
        boolean qualityGateOk = true

        echo "BRANCH_NAME detectada: ${branchName}"
        echo "shouldAbort = ${shouldAbort}"

        if (!qualityGateOk && shouldAbort) {
            error("Quality Gate FALLÓ y la heurística indica abortar. Abortando pipeline.")
        } else if (!qualityGateOk) {
            echo "Quality Gate FALLÓ, pero la heurística NO aborta. Continuando pipeline."
        } else {
            echo "Quality Gate OK -> Continuando pipeline."
        }
    }
}
