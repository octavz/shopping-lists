import React from 'react'
import {render} from 'react-dom'
import {createStore} from 'redux'
import {Provider} from 'react-redux'
import App from './containers/App'
import reducer from './reducers'
import 'todomvc-app-css/index.css'
import { Router, Route, IndexRoute, browserHistory } from 'react-router'
import { syncHistoryWithStore, routerReducer } from 'react-router-redux'
import Login from "./components/Login";
import Lists from "./components/Lists";
import MainSection from "./components/MainSection";

const store = createStore(reducer);

render(
    <Provider store={store}>
        <Router history={browserHistory}>
            <Route path="/" component={App}>
                <Route path="login" component={Login}/>
                <Route path="lists" component={Lists}/>
                <Route path="list/:id" component={MainSection}/>
            </Route>
        </Router>
    </Provider>,
    document.getElementById('root')
);
