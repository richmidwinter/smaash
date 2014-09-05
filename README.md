Smaash
======

Smaash is an authorization as a service implementation.

# Authentication

Smaash requires basic authentication for API calls. For this reason it's
paramount to put the service behind an SSL enabled webserver otherwise
authentication credentials will be sent in the clear.

It's suggested that an application accessing smaash is afforded its own
unique user. This will then be logged with the requests.

# Roles

Permission checks, setting of resource permissions and deleting resources
requires an authenticated user. All other API calls available require
admin access.

# Auditing

Smaash logs all access requests to the auditLog file defined in smaash.yml

# Configuration

The internal representation of groups is stored in a single number value
and both smaash and the administrators accessing the service need to be
aware of the bit positions in the representation, particularly for any
data migration.

# Example API calls

### Check if a user has permission to access a resource

    GET /smaash/{resource}/{user}
    
e.g.
    curl smaash/logo.jpg/jbloggs

### Set a user's access groups

    PUT /smaash/u/{user}
    
    With group names in a CSV body.

e.g.
    curl -X PUT -d "HR,LONDON" https://localhost/smaash/u/jbloggs
    
### Set a resource's access groups

    PUT /smaash/r/{resource}
    
    With group names in a CSV body.

e.g.
    curl -X PUT -d "HR" https://localhost/smaash/r/logo.jpg
    
### Set group positions

    PUT /smaash/positions
    
e.g.
    curl -X PUT -d "1: STAFF, \
    2: CONTRACTORS, \
    3: FINANCE, \
    4: HR, \
    5: MARKETING, \
    6: PR, \
    7: MANAGEMENT, \
    8: LONDON, \
    9: NEW YORK, \
    10: PARIS" https://localhost/smaash/positions
    
### List group positions

    GET /smaash/positions
    
e.g.
    curl https://localhost/smaash/positions

# Log viewing


