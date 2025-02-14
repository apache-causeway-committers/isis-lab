/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.lab.experiments.wktsel2.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;
import org.wicketstuff.select2.Select2Behavior;
import org.wicketstuff.select2.Select2Choice;
import org.wicketstuff.select2.Select2MultiChoice;
import org.wicketstuff.select2.Settings;
import org.wicketstuff.select2.StringTextChoiceProvider;

import org.apache.causeway.lab.experiments.wktsel2.WicketSelect2Application;

public class BasePage extends WebPage
{
	private static final long serialVersionUID = 1L;

	private static final int PAGE_SIZE = 10;
	@SuppressWarnings("unused")
	private Country country0 = Country.US;
	@SuppressWarnings("unused")
	private Country country = Country.US;
	@SuppressWarnings("unused")
	private Country countryDropDownChoice = Country.US;
	@SuppressWarnings("unused")
	private List<Country> countries = new ArrayList<>(Arrays.asList(new Country[] { Country.US, Country.CA }));
	@SuppressWarnings("unused")
	private List<Country> ajaxcountries = new ArrayList<>(Arrays.asList(new Country[] { Country.US, Country.CA }));
	@SuppressWarnings("unused")
	private List<Country> ajaxcountriesns = new ArrayList<>(Arrays.asList(new Country[] { Country.US, Country.CA }));
	@SuppressWarnings("unused")
	private List<Country> countriesListMultipleChoice = new ArrayList<>(Arrays.asList(new Country[] { Country.US, Country.CA }));
	@SuppressWarnings("unused")
	private List<String> tags = new ArrayList<>(Arrays.asList("tag1", "tag2"));
	@SuppressWarnings("unused")
	private List<Country> countriesStateless = new ArrayList<>(Arrays.asList(new Country[] { Country.US, Country.CA }));

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new BookmarkablePageLink<>("homeBootstrapPageLink", BootstrapPage.class));

		// single-select minimum example
		add(new Label("country0", new PropertyModel<>(this, "country0")));

		Select2Choice<Country> country0 = new Select2Choice<>("country0", new PropertyModel<Country>(
			this, "country0"), new CountriesProvider());
		defaultSelect2Settings(country0.getSettings())
				.setPlaceholder("Please select country")
				.setAllowClear(true);
		add(new Form<Void>("single0").add(country0));

		// single-select example
		add(new Label("country", new PropertyModel<>(this, "country")));

		Select2Choice<Country> country = new Select2Choice<>("country", new PropertyModel<Country>(
			this, "country"), new CountriesProvider());
		defaultSelect2Settings(country.getSettings())
				.setMinimumInputLength(1)
				.setPlaceholder("Please select country")
                .setCloseOnSelect(true)
				.setAllowClear(true);
		add(new Form<Void>("single").add(country));

		// single-select drop down choice
		add(new Label("countryDropDownChoice", new PropertyModel<>(this, "countryDropDownChoice")));

		DropDownChoice<Country> countryDropDownChoice = new DropDownChoice<Country>("countryDropDownChoice",
				new PropertyModel<Country>(this, "countryDropDownChoice"),
				new ListModel<Country>(Arrays.asList(Country.values())),
				new CountryRenderer());
		Select2Behavior countryDropDownChoiceSelect2Behavior = Select2Behavior.forSingleChoice();
		defaultSelect2Settings(countryDropDownChoiceSelect2Behavior.getSettings())
				.setPlaceholder("Please select country")
				.setAllowClear(true);
		countryDropDownChoice.add(countryDropDownChoiceSelect2Behavior);
		add(new Form<Void>("singleDropDownChoice").add(countryDropDownChoice));

		// multi-select example
		add(new Label("countries", new PropertyModel<>(this, "countries")));

		Select2MultiChoice<Country> countries = new Select2MultiChoice<>("countries",
			new PropertyModel<Collection<Country>>(this, "countries"), new CountriesProvider());
		defaultSelect2Settings(countries.getSettings())
				.setMinimumInputLength(1);
		add(new Form<Void>("multi").add(countries));

		// ajax multi-select example
		final Label ajaxLbl = new Label("ajaxcountries", new PropertyModel<>(this, "ajaxcountries"));
		add(ajaxLbl.setOutputMarkupId(true));

		Select2MultiChoice<Country> ajaxcountries = new Select2MultiChoice<>("ajaxcountries",
				new PropertyModel<Collection<Country>>(this, "ajaxcountries"), new CountriesProvider());
		defaultSelect2Settings(ajaxcountries.getSettings())
				.setMinimumInputLength(2);
		ajaxcountries.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(final AjaxRequestTarget target) {
				target.add(ajaxLbl);
			}
		});
		add(new Form<Void>("multiajax").add(ajaxcountries));

		// ajax multi-select example, no submit
		final Label ajaxLblns = new Label("ajaxcountriesnsm", new PropertyModel<>(this, "ajaxcountriesns"));
		add(ajaxLblns.setOutputMarkupId(true));

		final Select2MultiChoice<Country> ajaxcountriesns = new Select2MultiChoice<Country>("ajaxcountriesns",
				new PropertyModel<Collection<Country>>(this, "ajaxcountriesns"), new CountriesProvider())
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unused")
			public Collection<Country> getCurrent() {
				return getCurrentValue();
			}
		};
		final Label currentValue = new Label("ajaxcountriesns", new PropertyModel<Collection<String>>(ajaxcountriesns, "current"));
		add(currentValue.setOutputMarkupId(true));

		defaultSelect2Settings(ajaxcountriesns.getSettings())
				.setMinimumInputLength(2);
		ajaxcountriesns.add(new AjaxFormSubmitBehavior("change") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(final AjaxRequestTarget target) {
				target.add(ajaxLblns, currentValue, ajaxcountriesns);
			}

			@Override
			public boolean getDefaultProcessing() {
				return false;
			}
		});
		add(new Form<Void>("multiajaxns").add(ajaxcountriesns.setOutputMarkupId(true)));

		// multi-select example
		add(new Label("countriesListMultipleChoice", new PropertyModel<>(this, "countriesListMultipleChoice")));

		ListMultipleChoice<Country> countriesListMultipleChoice = new ListMultipleChoice<>("countriesListMultipleChoice",
			new PropertyModel<Collection<Country>>(this, "countriesListMultipleChoice"),
			new ListModel<Country>(Arrays.asList(Country.values())),
			new CountryRenderer()
		);
		Select2Behavior countriesListMultipleChoiceSelect2Behavior = Select2Behavior.forMultiChoice();
		defaultSelect2Settings(countriesListMultipleChoiceSelect2Behavior.getSettings());
		countriesListMultipleChoice.add(countriesListMultipleChoiceSelect2Behavior);
		add(new Form<Void>("listMultipleChoice").add(countriesListMultipleChoice));

		// tags example
		add(new Label("tagsLabel", new PropertyModel<>(this, "tags")));
		Select2MultiChoice<String> tags = new Select2MultiChoice<>("tagsSelect",
				new PropertyModel<>(this, "tags"), new TagProvider());
		defaultSelect2Settings(tags.getSettings())
				.setMinimumInputLength(1)
				.setCloseOnSelect(true)
				.setTags(true);

		// append "(new)" next to new tags (source http://stackoverflow.com/a/30021059)
		String tagNew = getString("tag.new");
		tags.getSettings().setCreateTag(
				"function (params) {\n" +
				"	return {\n" +
				"		id: params.term,\n" +
				"		text: params.term,\n" +
				"		newOption: true\n" +
				"	};\n" +
				"}");
		tags.getSettings().setTemplateResult(
				"function (data) {\n" +
				"	var result = $('<span></span>');\n" +
				"	result.text(data.text);\n" +
				"	if (data.newOption) {\n" +
				"		result.append(' <em>" + tagNew + "</em>');\n" +
				"	}\n" +
				"	return result;\n" +
				"}");
		add(new Form<Void>("tagsForm").add(tags));

		// stateless
		Form<Void> stateless = new Form<>("stateless");
		add(stateless);

		final Select2MultiChoice<Country> statelessCountries = new Select2MultiChoice<Country>("statelessCountries",
			new PropertyModel<Collection<Country>>(this, "countriesStateless"), new CountriesProvider())
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unused")
			public Collection<Country> getCurrent() {
				return getCurrentValue();
			}
		};
		defaultSelect2Settings(statelessCountries.getSettings())
				.setStateless(true)
				.setMountPath(Country.MOUNT_PATH);
		stateless.add(statelessCountries);
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(
		        new CssResourceReference(WicketSelect2Application.class, "css/bootstrap.css")));
	}

	protected Settings defaultSelect2Settings(final Settings settings) {
		return settings.setWidth("100%");
	}

	/**
	 * Queries {@code pageSize} worth of countries from the {@link Country} enum, starting with
	 * {@code page * pageSize} offset. Countries are matched on their {@code displayName} containing
	 * {@code term}
	 *
	 * @param term
	 *            search term
	 * @param page
	 *            starting page
	 * @param pageSize
	 *            items per page
	 * @return list of matches
	 */
	private static List<Country> queryMatches(String term, final int page, final int pageSize)
	{
		List<Country> result = new ArrayList<>();
		term = term == null ? "" : term.toUpperCase();
		final int offset = page * pageSize;

		int matched = 0;
		for (Country country : Country.values())
		{
			if (result.size() == pageSize)
			{
				break;
			}

			if (country.getDisplayName().toUpperCase().contains(term))
			{
				matched++;
				if (matched > offset)
				{
					result.add(country);
				}
			}
		}
		return result;
	}

	/**
	 * {@link Country} based choice provider for Select2 components. Demonstrates integration
	 * between Select2 components and a domain object (in this case an enum).
	 *
	 * @author igor
	 *
	 */
	public static class CountriesProvider extends ChoiceProvider<Country>
	{

		private static final long serialVersionUID = 1L;

		@Override
		public String getDisplayValue(final Country choice)
		{
			return choice.getDisplayName();
		}

		@Override
		public String getIdValue(final Country choice)
		{
			return choice.name();
		}

		@Override
		public void query(final String term, final int page, final Response<Country> response)
		{
			response.addAll(queryMatches(term, page, PAGE_SIZE));
			response.setHasMore(response.size() == PAGE_SIZE);
		}

		@Override
		public Collection<Country> toChoices(final Collection<String> ids)
		{
			ArrayList<Country> countries = new ArrayList<>();
			for (String id : ids)
			{
				countries.add(Country.valueOf(id));
			}
			return countries;
		}

	}

	public static class TagProvider extends StringTextChoiceProvider
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void query(final String term, final int page, final Response<String> response)
		{
			List<Country> matches = queryMatches(term, page, PAGE_SIZE);
			for (Country match : matches)
			{
				response.add(match.getDisplayName());
			}
			response.setHasMore(response.size() == PAGE_SIZE);
		}
	}

	public static class CountryRenderer extends ChoiceRenderer<Country>
	{

		private static final long serialVersionUID = 1L;

		public CountryRenderer() {
			super("displayName", "id");
		}

	}

}
