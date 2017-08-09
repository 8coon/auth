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
        </Switch>
    </BrowserRouter>,

    document.getElementById('root')
);

registerServiceWorker();

