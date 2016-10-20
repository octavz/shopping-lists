import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Register from '../components/Register'
import * as AppActions from '../actions'

const RegisterContainer = ({userData, actions}) => (
    <div>
        <Register userData={userData} actions={actions}/>
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
    actions: bindActionCreators(AppActions, dispatch)
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(RegisterContainer)
