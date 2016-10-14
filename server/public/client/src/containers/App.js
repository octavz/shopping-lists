import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from '../components/Home'
import Login from '../components/Login'
import * as TodoActions from '../actions'

const App = ({state, actions}) => (
    <div>
        <Login userData={state.userData} actions={actions}/>
    </div>
);


// App.propTypes = {
//     lists: PropTypes.array.isRequired,
//     user: PropTypes.object.isRequired,
//     actions: PropTypes.object.isRequired
// }

const mapStateToProps = state => ({
    data: state.items,
    userData: state.userData
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(TodoActions, dispatch)
})

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(App)
