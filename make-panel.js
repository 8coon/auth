const execSync = require('child_process').execSync;


const options = {
    cwd: `${__dirname}/assets/panel/`,
    stdio: [0, 1, 2],
};

execSync('npm install', options);
execSync('npm run-script build', options);
