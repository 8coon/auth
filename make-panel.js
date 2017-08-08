const execSync = require('child_process').execSync;


const options = {
    cwd: `${__dirname}/assets/panel/`
};

execSync('npm install', options);
execSync('npm run-script build', options);
