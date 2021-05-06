/*
 * For a detailed explanation regarding each configuration property, visit:
 * https://jestjs.io/docs/en/configuration.html
 */
module.exports = {
    reporters: ["default", ["jest-junit", {outputDirectory: "out"}]],
    globalSetup: './test/env/setup.js',
    globalTeardown: './test/env/teardown.js',
    testEnvironment: './test/env/testcontainersEnvironment.js',
};
