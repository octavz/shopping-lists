import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Login from '../components/Login'
import * as TodoActions from '../actions'

const LoginContainer = ({userData, actions}) => (
    <div>
        <Login userData={userData} actions={actions}/>
    </div>
);


// App.propTypes = {
//     lists: PropTypes.array.isRequired,
//     user: PropTypes.object.isRequired,
//     actions: PropTypes.object.isRequired
// }

const mapStateToProps = state => ({
    userData: state.items.userData
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(TodoActions, dispatch)
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(LoginContainer)
