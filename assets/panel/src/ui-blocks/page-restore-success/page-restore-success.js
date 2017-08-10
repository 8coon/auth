import React, {Component} from 'react';
import './page-restore-success.css';

// UI-Blocks
import LayoutLogin from './../layout-login/layout-login';


export default class PageRestoreSuccess extends Component {

    render() {
        return (
            <LayoutLogin>
                <div className="form">
                    <div className="form-block form__title">
                        Восстановить доступ
                    </div>

                    <div className="form-block">
                        На указанный вами Email было отправлено письмо с инструкциями по восстановлению.
                    </div>

                    <div className="form-block form__links">
                    </div>
                </div>
            </LayoutLogin>
        )
    }

}
