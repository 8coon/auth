import React from 'react';
import ReactDOM from 'react-dom';

//Router
import { BrowserRouter, Route, Switch } from 'react-router-dom';

// Font-Awesome
import 'font-awesome/css/font-awesome.min.css';

// Open Sans
import 'open-sans-all/css/open-sans.min.css';

// Globals
import './globals.css';
import './mobile.css';
import './form.css';

// UI-Blocks
import Application from './ui-blocks/application/application';
import PageLogin from './ui-blocks/page-login/page-login';
import PageSignup from './ui-blocks/page-signup/page-signup';
import PageRestoreAccess from './ui-blocks/page-restore-access/page-restore-access';
import PageRestoreSuccess from './ui-blocks/page-restore-success/page-restore-success';
import PagePasswordReset from './ui-blocks/page-password-reset/page-password-reset';

// Service Worker
import registerServiceWorker from './registerServiceWorker';

// Sitemap
import Sitemap from './sitemap';


ReactDOM.render(

    <BrowserRouter>
        <Switch>
            <Route exact path={Sitemap.root} component={Application}/>
            <Route exact path={Sitemap.login} component={PageLogin}/>
            <Route exact path={Sitemap.signup} component={PageSignup}/>
            <Route exact path={Sitemap.restoreAccess} component={PageRestoreAccess}/>
            <Route exact path={Sitemap.restoreSuccess} component={PageRestoreSuccess}/>
            <Route exact path={Sitemap.passwordReset} component={PagePasswordReset}/>
        </Switch>
    </BrowserRouter>,

    document.getElementById('root')
);

registerServiceWorker();

