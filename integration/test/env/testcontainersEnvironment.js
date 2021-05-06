const fs = require('fs');
const os = require('os');
const path = require('path');
const NodeEnvironment = require('jest-environment-node');

const DIR = path.join(os.tmpdir(), 'jest_testcontainers_global_setup');

module.exports = class TestContainersEnvironment extends NodeEnvironment {
    constructor(config) {
        super(config);
    }

    async setup() {
        await super.setup();
        // get the urls
        const appBaseUrl = fs.readFileSync(path.join(DIR, 'appBaseUrl'), 'utf8');
        const amqpBaseUrl = fs.readFileSync(path.join(DIR, 'amqpBaseUrl'), 'utf8');
        const mongoBaseUrl = fs.readFileSync(path.join(DIR, 'mongoBaseUrl'), 'utf8');
        if (!appBaseUrl || !amqpBaseUrl || !mongoBaseUrl) {
            throw new Error('baseUrls not found');
        }

        this.global.__APP_BASE_URL__ = appBaseUrl;
        this.global.__AMQP_BASE_URL__ = amqpBaseUrl;
        this.global.__MONGO_BASE_URL__ = mongoBaseUrl;
    }

    async teardown() {
        await super.teardown();
    }

    runScript(script) {
        return super.runScript(script);
    }
}
