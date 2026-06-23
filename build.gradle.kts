plugins {
    id("com.github.ElytraServers.elytra-conventions") version "v1.1.2.3"
    id("com.gtnewhorizons.gtnhconvention")
}

configurations.configureEach {
    resolutionStrategy {
        exclude(group = "com.github.GTNewHorizons", module = "CodeChickenLib")
        exclude(group = "net.glease", module = "tc4recipelib")
    }
}
