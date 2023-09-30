var express = require('express');
var app = express();
var YELP_KEY = "bvI2d8Z1GHPh1j7vSYWDaOw2dW9Cvs_TfUIKlGe0dO3lf_pfZpQdnMk4YjKDTF6wospsjs7tIijNziAwTnMOaIr-Gad32U9u8a_GvTar2bO-CLyAwKkEaguUsBg9Y3Yx";
var GOOGLE_KEY = "AIzaSyAd9b3YzW32cPZwJ3NdIVLpN_OCWIgvYU0";
var miles_km_factor = 0.62137119;
var axios = require('axios');
var cors = require('cors');
var yelp_url = 'https://api.yelp.com/v3/businesses/search';
var google_maps_url = "https://maps.googleapis.com/maps/api/geocode/json";
var yelp_detail_url = 'https://api.yelp.com/v3/businesses/';
var yelp_autocomplete_url = 'https://api.yelp.com/v3/autocomplete'

app.use(cors());

app.get('/searchbusiness', function(req, res) {
    var keyword = req.query.keyword;
    var category = req.query.category;
    var location = req.query.location;
    var miles = parseFloat(req.query.distance);
    var auto_checked = req.query.auto_checked;
    var LatLng = {};
    var meters = Math.min(40000, parseInt((miles / miles_km_factor) * 1000));
    if (auto_checked === 'true') {
        location = location.split(",");
        LatLng['lat'] = parseFloat(location[0]);
        LatLng['lng'] = parseFloat(location[1]);
        var callYelp = async() => {
            try {
                const yelp_response = await axios.get(yelp_url, {
                    params: {
                        'term': keyword, 'latitude': LatLng['lat'], 'longitude': LatLng['lng'],
                          'categories': category, 'radius': meters, 'limit': 10
                    },
                    headers: {
                        'Authorization': 'Bearer ' + YELP_KEY
                    }
                })
                return yelp_response.data;
            } catch(err) {
                return err;
            }
        };
        callYelp().then(yelp_value => {
            res.send(yelp_value);
        });
    } else {
        location = location.replace(' ', '+')
        location = location.replace(',', '+')
        var getGoogleInfo = async () => {
            try {
                    const response = await axios.get(google_maps_url, {
                        params: {
                            'address': location,
                            'key': GOOGLE_KEY
                        }
                    })
                return response.data.results[0].geometry.location;
            } catch(err) {
                return err;
            }
        };
        getGoogleInfo().then(value => {
            var callYelp = async() => {
                try {
                    const yelp_response = await axios.get(yelp_url, {
                        params: {
                            'term': keyword, 'latitude': value.lat, 'longitude': value.lng,
                              'categories': category, 'radius': meters, 'limit': 10
                        },
                        headers: {
                            'Authorization': 'Bearer ' + YELP_KEY
                        }
                    })
                    return yelp_response.data;
                } catch(err) {
                    return err;
                }
            };
            callYelp().then(yelp_value => {
                res.send(yelp_value);
            })
        });
    }    
});

app.get('/searchdetail', function(req, res) {
    var yelp_id = req.query.id;
    var businessDetail = async() => {
        try {
            const yelp_response = await axios.get(yelp_detail_url + yelp_id, {
                headers: {
                    'Authorization': 'Bearer ' + YELP_KEY
                }
            })
            return yelp_response.data;
        } catch(err) {
            return err;
        }
    };
    businessDetail().then(yelp_value => {
        res.send(yelp_value);
    });
});

app.get('/business_review', function(req, res) {
    var yelp_id = req.query.id;
    var getReview = async() => {
        try {
            const yelp_response = await axios.get(yelp_detail_url + yelp_id + "/reviews", {
                headers: {
                    'Authorization': 'Bearer ' + YELP_KEY
                }
            })
            return yelp_response.data;
        } catch(err) {
            return err;
        }
    };
    getReview().then(reviews => {
        res.send(reviews);
    });
});

app.get('/autocomplete', function(req, res) {
    var keyword = req.query.keyword;
    var getAutocomplete = async() => {
        try {
            const yelp_response = await axios.get(yelp_autocomplete_url, {
                params: {
                    'text': keyword
                },
                headers: {
                    'Authorization': 'Bearer ' + YELP_KEY
                }
            })
            return yelp_response.data;
        } catch(err) {
            return err;
        }
    };
    getAutocomplete().then(reviews => {
        res.send(reviews);
    });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}...`);
});
