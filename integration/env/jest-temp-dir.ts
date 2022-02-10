import * as os from 'node:os'
import * as path from 'node:path'

export default path.join(os.tmpdir(), 'jest_testcontainers_global_setup')
