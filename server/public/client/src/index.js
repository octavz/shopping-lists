import React from 'react'
import {render} from 'react-dom'
import {createStore} from 'redux'
import {Provider} from 'react-redux'
import reducer from './reducers'
import 'todomvc-app-css/index.css'
import {Router, Route, browserHistory} from 'react-router'
import LoginContainer from "./containers/LoginContainer";
import App from "./containers/AppContainer";
import MainSection from "./components/MainSection";

const store = createStore(reducer);

render(
    <Provider store={store}>
        <Router history={browserHistory}>
            <Route path="/" component={LoginContainer}>
                <Route path="login" component={LoginContainer}/>
                <Route path="lists" component={App}/>
                <Route path="list/:id" component={MainSection}/>
            </Route>
        </Router>
    </Provider>,
    document.getElementById('root')
);
