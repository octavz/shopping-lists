(ns web.views.commons)

(defn row [label input id]
  [:div.row
    [:label.col-sm-2.control-label (str label ":")]
    [:div.col-sm-5 [:input.form-control {:field input :id id :placeholder label}]]])
