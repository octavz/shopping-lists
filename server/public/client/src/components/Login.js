import React, {Component, PropTypes} from 'react'
import {Link} from 'react-router'

export default class Login extends Component {
    static propTypes = {
        userData: PropTypes.object.isRequired,
        actions: PropTypes.object.isRequired
    };

    handleLogin = e => {
        console.log("doing login")
    };

    state = {
        login: '', password: '', isAuthenticated: false
    };

    handleLoginChange = e => {
        this.setState({...this.state, login: e.target.value });
        console.log(this.state);
    };

    handlePassChange = e => {
        this.setState({...this.state, password: e.target.value});
        console.log(this.state);
    };

    render() {
        return (
            <form className="form-horizontal">
                <div className="form-group">
                    <label htmlFor="inputEmail3" className="col-sm-2 control-label">Email</label>
                    <div className="col-sm-10">
                        <input type="email" className="form-control" id="inputEmail3" placeholder="Email"
                               value={this.state.login} onChange={this.handleLoginChange}/>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="inputPassword3" className="col-sm-2 control-label">Password</label>
                    <div className="col-sm-10">
                        <input type="password"
                               className="form-control" id="inputPassword3" placeholder="Password"
                               value={this.state.password}
                               onChange={this.handlePassChange}/>
                    </div>
                </div>
                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <div className="checkbox">
                            <label>
                                <input type="checkbox"/> Remember me
                            </label>
                        </div>
                    </div>
                </div>
                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <button type="button" onClick={this.handleLogin} className="btn btn-default">Sign in</button>
                    </div>
                </div>
                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <Link to="/register">Register</Link>
                    </div>
                </div>
            </form>
        )
    }
}
