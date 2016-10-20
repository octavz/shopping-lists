import React from 'react'
import {render} from 'react-dom'
import {Provider} from 'react-redux'
import {Router, Route, IndexRoute, browserHistory} from 'react-router'
import RegisterContainer from "./containers/RegisterContainer";
import AppContainer from "./containers/AppContainer";
import ItemsContainer from "./containers/ItemsContainer";
import {syncHistoryWithStore} from 'react-router-redux'
import store from './store'


const history = syncHistoryWithStore(browserHistory, store);

render(
    <Provider store={store}>
        <Router history={history}>
            <Route path="/">
                <IndexRoute component={AppContainer}/>
                <Route path="login" component={AppContainer}/>
                <Route path="register" component={RegisterContainer}/>
                <Route path="lists" component={AppContainer}/>
                <Route path="list/:id" component={ItemsContainer}/>
            </Route>
        </Router>
    </Provider>,
    document.getElementById('root')
);
