plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.springBoot)
    implementation(libs.plugin.springDependencyManagement)
    implementation(libs.plugin.ktlint)
    implementation("com.dorongold.plugins:task-tree:2.1.0")
    implementation(kotlin("allopen"))
    implementation(kotlin("gradle-plugin"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("noarg"))
}
