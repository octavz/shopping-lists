import React from 'react'
import {render} from 'react-dom'
import {createStore, applyMiddleware} from 'redux'
import {Provider} from 'react-redux'
import reducer from './reducers'
import 'todomvc-app-css/index.css'
import {Router, Route, IndexRoute, browserHistory} from 'react-router'
import RegisterContainer from "./containers/RegisterContainer";
import AppContainer from "./containers/AppContainer";
import ItemsContainer from "./containers/ItemsContainer";
import thunk from 'redux-thunk';

const store = createStore(reducer, thunk);

render(
    <Provider store={store}>
        <Router history={browserHistory}>
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
