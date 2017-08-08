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

// UI-Blocks
import Application from './ui-blocks/application/application';
import PageLogin from './ui-blocks/page-login/page-login';

// Service Worker
import registerServiceWorker from './registerServiceWorker';

// Models
import User from 'minecraftshire-jsapi/models/User/user';


// Application Root
const appRoot = location.hostname.startsWith('localhost') ? '' : '/app';


ReactDOM.render(

    <BrowserRouter>
        <Switch>
            <Route exact path={`${appRoot}/`} component={Application}/>
            <Route path={`${appRoot}/login`} component={PageLogin}/>
        </Switch>
    </BrowserRouter>,

    document.getElementById('root')
);

registerServiceWorker();
console.log(new User());

