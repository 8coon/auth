
export default class Form {

    fields = {};
    errors = [];

    add(name) {
        return (field) => {
            this.fields[name] = field;
            return name;
        }
    }

    forEach(callback) {
        Object.keys(this.fields).forEach((key, idx) => {
            callback(this.fields[key], idx);
        });
    }

    map(callback) {
        return Object.keys(this.fields).map((key, idx) => {
            return callback(this.fields[key], idx);
        })
    }

    validate() {
        let result = true;

        this.errors = this.map(field => {
            const nextResult = field.validate() === true;
            result = result && nextResult;
            return nextResult;
        });

        this.toggleTooltip();
        return result;
    }

    toggleTooltip(visible = null) {
        if (visible !== null) {
            this.forEach(field => field.toggleTooltip(visible));
            return;
        }

        let result = true;

        this.forEach((field, idx) => {
            if (result && !this.errors[idx]) {
                field.toggleTooltip(true);
            } else {
                field.toggleTooltip(false);
            }

            result = result && this.errors[idx];
        });
    }

}
