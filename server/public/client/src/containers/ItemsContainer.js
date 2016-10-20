import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from '../components/Home'
import * as AppActions from '../actions'

const ItemsContainer = ({lists, actions}) => (
    <div>
        <Home lists={lists} actions={actions}/>
    </div>
);


// App.propTypes = {
//     lists: PropTypes.array.isRequired,
//     user: PropTypes.object.isRequired,
//     actions: PropTypes.object.isRequired
// }

const mapStateToProps = state => ({
    lists: state.lists,
});

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(AppActions, dispatch)
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(ItemsContainer)
