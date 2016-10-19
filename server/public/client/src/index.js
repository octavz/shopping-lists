import React from 'react'
import {render} from 'react-dom'
import {createStore} from 'redux'
import {Provider} from 'react-redux'
import reducer from './reducers'
import 'todomvc-app-css/index.css'
import {Router, Route, IndexRoute, browserHistory} from 'react-router'
import RegisterContainer from "./containers/RegisterContainer";
import AppContainer from "./containers/AppContainer";
import ItemsContainer from "./containers/ItemsContainer";

const store = createStore(reducer);

render(
    <Provider store={store}>
        <Router history={browserHistory}>
            <Route path="/" >
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
