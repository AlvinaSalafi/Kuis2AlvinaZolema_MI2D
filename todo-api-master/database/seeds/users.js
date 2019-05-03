
exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('users').del()
    .then(function () {
      // Inserts seed entries
      return knex('users').insert([
        {username: 'alvina', name: 'alvina', password: 'alvina'},
        {username: 'zolema', name: 'zolema', password: 'zolema'},
        {username: 'salafi', name: 'salafi', password: 'salafi'}
      ]);
    });
};
