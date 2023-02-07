import { rimraf } from 'rimraf'
import jestTempDir from '../jest-temp-dir'

module.exports = async function () {
  // remove temp folder
  await rimraf(jestTempDir)
  // stop containers
  await globalThis.dockerComposeEnvironment.down()
}
