
// Application Root
const appRoot = window.location.hostname.startsWith('localhost') ? '' : '/app';


export default {
    root: `${appRoot}/`,
    login: `${appRoot}/login`,
    signup: `${appRoot}/signup`,
};
