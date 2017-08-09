
export default class Form {

    fields = {};

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

    validate() {
        let result = true;

        this.forEach(field => {
            result = result && field.validate() === true;
        });

        this.toggleTooltip(true);
        return result;
    }

    toggleTooltip(visible) {
        let result = true;

        this.forEach(field => {
            const nextResult = result && field.state.error !== null;

            if ((!nextResult && result) || visible) {
                field.toggleTooltip(visible);
            } else if (visible) {
                field.toggleTooltip(false);
            }

            result = nextResult;
        });
    }

}
