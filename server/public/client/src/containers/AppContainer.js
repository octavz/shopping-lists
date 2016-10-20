import React, {Component, PropTypes} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as AppActions from '../actions'
import Header from '../components/Header'
import Lists from '../components/Lists'
import Login from '../components/Login'
import store from '../store'

class AppContainer extends Component {

    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
    }

    static contextTypes = {
        router: PropTypes.object.isRequired
    };

    handleLogin(login, pass) {
        return store.dispatch(AppActions.loginUser(login, pass));
    }

    componentWillMount() {
        if (this.props.state.userData.loggedIn) {
            this.context.router.push('/lists');
        } else {
            this.context.router.push('/login');
        }
    }

    render() {
        const {state, actions} = this.props;
        if (state.userData.loggedIn) {
            return (
                <div>
                    {<Header addTodo={actions.addList}/>}
                    {<Lists lists={state.lists} actions={actions}/>}
                </div>
            )
        } else {
            return (
                <Login userData={state.userData} onLogin={this.handleLogin}/>
            )
        }
    }
}

AppContainer.propTypes = {
    actions: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
    state: state.items,
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(AppActions, dispatch)
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AppContainer)
