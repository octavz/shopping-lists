import React, {Component, PropTypes} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as AppActions from '../actions'
import Header from '../components/Header'
import Lists from '../components/Lists'
import Login from '../components/Login'

class AppContainer extends Component {

    static contextTypes = {
        router: PropTypes.object
    };

    componentWillMount() {
        if (this.props.state.userData.loggedIn) {
            this.context.router.push('/lists');
        } else {
            this.context.router.push('/login');
        }
    }

    render() {
        var {state, actions} = this.props;
        if (state.userData.loggedIn) {
            return (
                <div>
                    {<Header addTodo={actions.addList}/>}
                    {<Lists lists={state.lists} actions={actions}/>}
                </div>
            )
        } else {
            return (
                <Login userData={state.userData} actions={actions}/>
            )
        }
    }
}

// App.propTypes = {
//     lists: PropTypes.array.isRequired,
//     user: PropTypes.object.isRequired,
//     actions: PropTypes.object.isRequired
// }

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
