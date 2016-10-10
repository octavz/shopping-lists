import React from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import Home from '../components/Home'
import * as TodoActions from '../actions'

const App = ({data, actions}) => (
    <div>
        <Home data={data} actions={actions}/>
    </div>
)

// App.propTypes = {
//     lists: PropTypes.array.isRequired,
//     user: PropTypes.object.isRequired,
//     actions: PropTypes.object.isRequired
// }

const mapStateToProps = state => ({
    data: state.items
})

const mapDispatchToProps = dispatch => ({
    actions: bindActionCreators(TodoActions, dispatch)
})

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(App)
