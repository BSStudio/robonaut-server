import os from 'os'
import path from 'path'

export default path.join(os.tmpdir(), 'jest_testcontainers_global_setup')
