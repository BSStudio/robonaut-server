const os = require('os');
const path = require('path');
const rimraf = require('rimraf');

const DIR = path.join(os.tmpdir(), 'jest_testcontainers_global_setup');

module.exports = async function () {

    // stop containers
    await global.__DOCKER_COMPOSE_ENV__.down();

    // remove temp folder
    rimraf.sync(DIR);

};