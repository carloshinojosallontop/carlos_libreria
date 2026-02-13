def call(Map config = [:]) {
    // Parámetro abortPipeline (por defecto false)
    boolean abortPipeline = config.get('abortPipeline', false) as boolean

    // Simulación de análisis estático (sin SonarQube real)
    timeout(time: 5, unit: 'MINUTES') {
        sh 'echo "Ejecución de las pruebas de calidad de código"'

        // Simulamos resultado del Quality Gate (true = pasa, false = falla)
        boolean qualityGateOk = true

        if (!qualityGateOk && abortPipeline) {
            error("Quality Gate FALLÓ. abortPipeline=true -> Abortando pipeline.")
        } else if (!qualityGateOk) {
            echo "Quality Gate FALLÓ, pero abortPipeline=false -> Continuando pipeline."
        } else {
            echo "Quality Gate OK -> Continuando pipeline."
        }
    }
}
