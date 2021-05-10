import rimraf from 'rimraf'
import testEnvironmentTempDir from './testEnvironmentTempDir'

module.exports = async function () {
  // stop containers
  await global.__DOCKER_COMPOSE_ENVIRONMENT__.down()

  // remove temp folder
  rimraf.sync(testEnvironmentTempDir)
}
