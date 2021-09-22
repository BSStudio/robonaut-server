import rimraf = require('rimraf')
import jestTempDir from './jestTempDir'

module.exports = async function () {
  // remove temp folder
  rimraf.sync(jestTempDir)
  // stop containers
  await global.dockerComposeEnvironment.down()
}
